# Billing Discounts API - Makefile

.PHONY: help clean compile test package run docker-up docker-down docker-restart lint format

# Default target
help:
	@echo "Available targets:"
	@echo "  clean          Clean project (mvn clean)"
	@echo "  compile        Compile the project"
	@echo "  test           Run tests"
	@echo "  package        Package the application"
	@echo "  run            Run the application"
	@echo "  docker-up      Start MongoDB with Docker"
	@echo "  docker-down    Stop MongoDB Docker containers"
	@echo "  docker-restart Restart MongoDB Docker containers"
	@echo "  lint           Run code quality checks"
	@echo "  format         Format code"

# Maven targets
clean:
	@echo "Cleaning project..."
	mvn clean

compile:
	@echo "Compiling project..."
	mvn compile

test:
	@echo "Running tests..."
	mvn test

package:
	@echo "Packaging application..."
	mvn package -DskipTests

run:
	@echo "Starting application..."
	mvn spring-boot:run

# Docker targets
docker-up:
	@echo "Starting MongoDB with Docker..."
	docker-compose up -d

docker-down:
	@echo "Stopping Docker containers..."
	docker-compose down

docker-restart: docker-down docker-up
	@echo "Docker containers restarted"

# Code quality targets
lint:
	@echo "Running code quality checks..."
	mvn compile

format:
	@echo "Code formatting (placeholder - add your formatter here)"
	@echo "No formatter configured yet"

# Combined targets
dev-start: docker-up run
	@echo "Development environment started"

dev-stop: docker-down
	@echo "Development environment stopped"

build: clean compile package
	@echo "Full build completed"