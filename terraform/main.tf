terraform {
  required_providers {
    google = {
      source  = "hashicorp/google"
      version = "~> 4.0"
    }
    random = {
      source  = "hashicorp/random"
      version = "~> 3.1"
    }
  }
}

provider "google" {
  project = var.project_id
  region  = var.region
}

# Use existing VPC network
data "google_compute_network" "vpc_network" {
  name = "billing-discounts-api-vpc-1de33de5"
}

resource "google_compute_firewall" "mongo_firewall" {
  name    = "${var.service_name}-mongo-firewall"
  network = data.google_compute_network.vpc_network.name

  allow {
    protocol = "tcp"
    ports    = ["27017"]
  }

  source_ranges = ["0.0.0.0/0"]
  
  lifecycle {
    ignore_changes = [creation_timestamp]
  }
}

resource "google_compute_disk" "mongodb_data" {
  name  = "${var.service_name}-mongodb-data"
  type  = "pd-standard"
  zone  = "${var.region}-a"
  size  = 10
  
  lifecycle {
    prevent_destroy = true
    ignore_changes = [creation_timestamp]
  }
}

resource "google_compute_instance" "mongodb" {
  name         = "${var.service_name}-mongodb-vm"
  machine_type = "e2-micro"
  zone         = "${var.region}-a"

  boot_disk {
    initialize_params {
      image = "debian-cloud/debian-11"
    }
  }

  attached_disk {
    source = google_compute_disk.mongodb_data.id
    device_name = "mongodb-data"
  }

  network_interface {
    network = data.google_compute_network.vpc_network.name
    access_config {}
  }

  metadata_startup_script = file("../scripts/setup-mongodb-vm.sh")

  tags = ["mongodb"]
  
  lifecycle {
    prevent_destroy = true
    ignore_changes = [creation_timestamp, metadata_startup_script]
  }
}


resource "google_project_service" "required_apis" {
  for_each = toset([
    "run.googleapis.com",
    "compute.googleapis.com",
    "artifactregistry.googleapis.com"
  ])
  
  project = var.project_id
  service = each.key
}

resource "google_cloud_run_v2_service" "default" {
  name     = var.service_name
  location = var.region

  template {
    containers {
      image = var.image_path
      ports {
        container_port = 8080
      }
      env {
        name  = "MONGODB_URI"
        value = "mongodb://${google_compute_instance.mongodb.network_interface[0].access_config[0].nat_ip}:27017/billing_discounts"
      }
      env {
        name  = "SPRING_PROFILES_ACTIVE"
        value = "prod"
      }
      resources {
        limits = {
          cpu    = "1"
          memory = "512Mi"
        }
      }
    }
    timeout = "300s"
  }

  depends_on = [google_compute_instance.mongodb]
}

resource "google_cloud_run_service_iam_member" "noauth" {
  location = google_cloud_run_v2_service.default.location
  service  = google_cloud_run_v2_service.default.name
  role     = "roles/run.invoker"
  member   = "allUsers"
}