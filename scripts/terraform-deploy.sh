#!/bin/sh
set -e

echo "Starting Terraform deployment..."

cd terraform

echo "Initializing Terraform..."
terraform init

echo "Importing existing resources..."
../scripts/import-existing-resources.sh

echo "Planning Terraform deployment..."
terraform plan \
  -var="project_id=${PROJECT_ID:-ai-credit-product}" \
  -var="region=${REGION:-us-central1}" \
  -var="image_path=${IMAGE_PATH}"

echo "Applying Terraform deployment..."
terraform apply -auto-approve \
  -var="project_id=${PROJECT_ID:-ai-credit-product}" \
  -var="region=${REGION:-us-central1}" \
  -var="image_path=${IMAGE_PATH}"

echo "Terraform deployment completed successfully"