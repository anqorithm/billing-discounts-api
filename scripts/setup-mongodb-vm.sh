#!/bin/bash
echo "Setting up MongoDB on VM..."

if ! command -v docker &> /dev/null; then
    sudo apt-get update
    sudo apt-get install -y apt-transport-https ca-certificates curl gnupg2 software-properties-common
    curl -fsSL https://download.docker.com/linux/debian/gpg | sudo apt-key add -
    sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/debian $(lsb_release -cs) stable"
    sudo apt-get update
    sudo apt-get install -y docker-ce
fi

sudo mkfs.ext4 -F /dev/disk/by-id/google-mongodb-data
sudo mkdir -p /data/db
sudo mount /dev/disk/by-id/google-mongodb-data /data/db
echo '/dev/disk/by-id/google-mongodb-data /data/db ext4 defaults 0 0' | sudo tee -a /etc/fstab
sudo chown -R 999:999 /data/db

sudo docker stop mongodb || true
sudo docker rm mongodb || true

sudo docker run -d -p 27017:27017 --name mongodb -v /data/db:/data/db mongo:5.0

echo "MongoDB setup completed"