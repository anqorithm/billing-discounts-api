#!/bin/bash
set -e

echo "Building and pushing Docker image..."

PROJECT_ID=${PROJECT_ID:-ai-credit-product}
REGION=${REGION:-us-central1}
REPO_NAME=${REPO_NAME:-billing-discounts-api-repo}
SERVICE_NAME=${SERVICE_NAME:-billing-discounts-api}

IMAGE_PATH="${REGION}-docker.pkg.dev/${PROJECT_ID}/${REPO_NAME}/${SERVICE_NAME}:latest"

echo "IMAGE_PATH will be: ${IMAGE_PATH}"
echo "Assuming JAR is already built by previous step"

echo "Building Docker image: ${IMAGE_PATH}"
docker build -t "${IMAGE_PATH}" .

echo "Pushing Docker image..."
docker push "${IMAGE_PATH}"

echo "Build and push completed successfully"
echo "Final IMAGE_PATH=${IMAGE_PATH}"