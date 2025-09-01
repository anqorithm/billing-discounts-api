variable "project_id" {
  description = "GCP project ID"
  type        = string
}

variable "region" {
  description = "GCP region for resources"
  type        = string
}

variable "image_path" {
  description = "Container image for Cloud Run (e.g. gcr.io/<project>/<image>:tag)"
  type        = string
}

variable "service_name" {
  description = "Base name for resources and Cloud Run service"
  type        = string
  default     = "billing-discounts-api"
}

variable "vpc_name" {
  description = "Name of the existing VPC network to use"
  type        = string
  default     = "default"
}

variable "mongodb_disk_size_gb" {
  description = "Persistent disk size for MongoDB data volume (GB)"
  type        = number
  default     = 10
}

variable "mongodb_allowed_cidrs" {
  description = "CIDR blocks allowed to access MongoDB on port 27017 (consider restricting)"
  type        = list(string)
  default     = ["0.0.0.0/0"]
}
