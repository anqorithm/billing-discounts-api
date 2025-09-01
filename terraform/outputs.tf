output "service_url" {
  value = google_cloud_run_v2_service.default.uri
}

output "mongodb_external_ip" {
  value = google_compute_instance.mongodb.network_interface[0].access_config[0].nat_ip
}
