#!/bin/bash

set -e

# Configuration
PROJECT_ID=${1:-"ai-credit-product"}
REGION=${2:-"us-central1"}
SERVICE_NAME="billing-discounts-api"

echo "🚀 Deploying Billing Discounts API using Cloud Build"
echo "Project: $PROJECT_ID"
echo "Region: $REGION"

echo "☁️ Triggering Cloud Build deployment..."
gcloud builds submit --config=cloudbuild.yaml --project=$PROJECT_ID

echo "✅ Deployment completed via Cloud Build!"
echo "🌐 Check Cloud Build logs for service URL"