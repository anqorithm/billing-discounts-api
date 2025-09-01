#!/bin/sh
echo "Importing existing resources into Terraform state..."

cd terraform

if terraform state list | grep "google_compute_firewall.mongo_firewall" > /dev/null 2>&1; then
    echo "Firewall already in state"
else
    echo "Importing firewall rule..."
    terraform import google_compute_firewall.mongo_firewall projects/ai-credit-product/global/firewalls/billing-discounts-api-mongo-firewall || true
fi

if terraform state list | grep "google_compute_disk.mongodb_data" > /dev/null 2>&1; then
    echo "Disk already in state"
else
    echo "Importing persistent disk..."
    terraform import google_compute_disk.mongodb_data projects/ai-credit-product/zones/us-central1-a/disks/billing-discounts-api-mongodb-data || true
fi

if terraform state list | grep "google_compute_instance.mongodb" > /dev/null 2>&1; then
    echo "MongoDB VM already in state"
else
    echo "Importing MongoDB VM..."
    terraform import google_compute_instance.mongodb projects/ai-credit-product/zones/us-central1-a/instances/billing-discounts-api-mongodb-vm || true
fi

if terraform state list | grep "google_cloud_run_v2_service.default" > /dev/null 2>&1; then
    echo "Cloud Run service already in state"
else
    echo "Importing Cloud Run service..."
    terraform import google_cloud_run_v2_service.default projects/ai-credit-product/locations/us-central1/services/billing-discounts-api || true
fi

if terraform state list | grep "google_cloud_run_service_iam_member.noauth" > /dev/null 2>&1; then
    echo "Cloud Run IAM member already in state"
else
    echo "Importing Cloud Run IAM member..."
    terraform import google_cloud_run_service_iam_member.noauth "projects/ai-credit-product/locations/us-central1/services/billing-discounts-api roles/run.invoker allUsers" || true
fi

echo "Import process completed"