# Colors for output
RED := \033[31m
GREEN := \033[32m
YELLOW := \033[33m
BLUE := \033[34m
MAGENTA := \033[35m
CYAN := \033[36m
WHITE := \033[37m
BOLD := \033[1m
RESET := \033[0m

# Project variables
PROJECT_NAME := billing-discounts-api
DOCKER_COMPOSE := docker-compose
MAVEN := mvn

.PHONY: help
help: ## Show this help message
	@echo "$(BOLD)$(CYAN)Available commands:$(RESET)"
	@awk 'BEGIN {FS = ":.*##"; printf "$(BOLD)$(CYAN)%-20s$(RESET) $(WHITE)%s$(RESET)\n", "Command", "Description"} /^[a-zA-Z_-]+:.*?##/ { printf "$(YELLOW)%-20s$(RESET) %s\n", $$1, $$2 }' $(MAKEFILE_LIST)

.PHONY: clean
clean: ## Clean Maven build artifacts
	@echo "$(BOLD)$(YELLOW)Cleaning Maven build artifacts...$(RESET)"
	$(MAVEN) clean
	@echo "$(BOLD)$(GREEN)Maven clean completed!$(RESET)"

.PHONY: compile
compile: ## Compile the project
	@echo "$(BOLD)$(BLUE)Compiling project...$(RESET)"
	$(MAVEN) compile
	@echo "$(BOLD)$(GREEN)Compilation completed!$(RESET)"

.PHONY: test
test: ## Run unit tests
	@echo "$(BOLD)$(MAGENTA)Running unit tests...$(RESET)"
	$(MAVEN) test
	@echo "$(BOLD)$(GREEN)Unit tests completed!$(RESET)"

.PHONY: test-coverage
test-coverage: ## Run tests and generate coverage report
	@echo "$(BOLD)$(MAGENTA)Running tests with coverage...$(RESET)"
	$(MAVEN) clean test jacoco:report
	@echo "$(BOLD)$(GREEN)Test coverage report generated!$(RESET)"
	@echo "$(CYAN)Coverage report available at: target/site/jacoco/index.html$(RESET)"

.PHONY: package
package: ## Package the application
	@echo "$(BOLD)$(BLUE)Packaging application...$(RESET)"
	$(MAVEN) package -DskipTests
	@echo "$(BOLD)$(GREEN)Application packaged!$(RESET)"

.PHONY: build
build: ## Build the application (clean, compile, test, package)
	@echo "$(BOLD)$(CYAN)Building application...$(RESET)"
	$(MAVEN) clean compile test package
	@echo "$(BOLD)$(GREEN)Application build completed!$(RESET)"

.PHONY: run
run: ## Run the application locally
	@echo "$(BOLD)$(GREEN)Starting application locally...$(RESET)"
	$(MAVEN) spring-boot:run -Dspring-boot.run.profiles=dev
	@echo "$(BOLD)$(GREEN)Application started!$(RESET)"

.PHONY: docker-build
docker-build: ## Build Docker image
	@echo "$(BOLD)$(BLUE)Building Docker image...$(RESET)"
	docker build -t $(PROJECT_NAME):latest .
	@echo "$(BOLD)$(GREEN)Docker image built successfully!$(RESET)"

.PHONY: docker-up
docker-up: ## Start all services with Docker Compose
	@echo "$(BOLD)$(CYAN)Starting all services with Docker Compose...$(RESET)"
	$(DOCKER_COMPOSE) up -d
	@echo "$(BOLD)$(GREEN)All services started!$(RESET)"
	@echo "$(YELLOW)API available at: http://localhost:8080$(RESET)"
	@echo "$(YELLOW)MongoDB available at: localhost:27017$(RESET)"
	@echo "$(YELLOW)Mongo Express available at: http://localhost:8081$(RESET)"
	@echo "$(YELLOW)SonarQube available at: http://localhost:9000$(RESET)"

.PHONY: docker-down
docker-down: ## Stop all services
	@echo "$(BOLD)$(RED)Stopping all services...$(RESET)"
	$(DOCKER_COMPOSE) down
	@echo "$(BOLD)$(GREEN)All services stopped!$(RESET)"

.PHONY: docker-restart
docker-restart: docker-down docker-up ## Restart all services

.PHONY: docker-logs
docker-logs: ## Show logs for all services
	@echo "$(BOLD)$(CYAN)Showing Docker Compose logs...$(RESET)"
	$(DOCKER_COMPOSE) logs -f

.PHONY: docker-logs-app
docker-logs-app: ## Show logs for the application service
	@echo "$(BOLD)$(CYAN)Showing application logs...$(RESET)"
	$(DOCKER_COMPOSE) logs -f app

.PHONY: docker-logs-mongo
docker-logs-mongo: ## Show logs for MongoDB service
	@echo "$(BOLD)$(CYAN)Showing MongoDB logs...$(RESET)"
	$(DOCKER_COMPOSE) logs -f mongodb

.PHONY: docker-logs-mongo-express
docker-logs-mongo-express: ## Show logs for Mongo Express service
	@echo "$(BOLD)$(CYAN)Showing Mongo Express logs...$(RESET)"
	$(DOCKER_COMPOSE) logs -f mongo-express

.PHONY: docker-shell-app
docker-shell-app: ## Get shell access to the application container
	@echo "$(BOLD)$(MAGENTA)Opening shell in application container...$(RESET)"
	docker exec -it billing-discounts-api /bin/bash

.PHONY: docker-shell-mongo
docker-shell-mongo: ## Get shell access to MongoDB container
	@echo "$(BOLD)$(MAGENTA)Opening MongoDB shell...$(RESET)"
	docker exec -it billing-discounts-mongo mongosh

.PHONY: sonar-run
sonar-run: ## Run SonarQube analysis
	@echo "$(BOLD)$(BLUE)Running SonarQube analysis...$(RESET)"
	$(MAVEN) clean test jacoco:report sonar:sonar \
		-Dsonar.host.url=http://localhost:9000 \
		-Dsonar.login=admin \
		-Dsonar.password=admin
	@echo "$(BOLD)$(GREEN)SonarQube analysis completed!$(RESET)"
	@echo "$(CYAN)SonarQube dashboard: http://localhost:9000$(RESET)"

.PHONY: lint
lint: ## Run code formatting and linting
	@echo "$(BOLD)$(YELLOW)Running code formatting...$(RESET)"
	$(MAVEN) spotless:apply
	@echo "$(BOLD)$(GREEN)Code formatting completed!$(RESET)"

.PHONY: checkstyle
checkstyle: ## Run checkstyle analysis
	@echo "$(BOLD)$(BLUE)Running checkstyle analysis...$(RESET)"
	$(MAVEN) checkstyle:check
	@echo "$(BOLD)$(GREEN)Checkstyle analysis completed!$(RESET)"

.PHONY: integration-test
integration-test: ## Run integration tests
	@echo "$(BOLD)$(MAGENTA)Running integration tests...$(RESET)"
	$(MAVEN) verify -Dskip.unit.tests=true
	@echo "$(BOLD)$(GREEN)Integration tests completed!$(RESET)"

.PHONY: dev-setup
dev-setup: ## Setup development environment
	@echo "$(BOLD)$(CYAN)Setting up development environment...$(RESET)"
	$(DOCKER_COMPOSE) up -d mongodb mongo-express
	@echo "$(BOLD)$(GREEN)Development environment ready!$(RESET)"
	@echo "$(YELLOW)MongoDB: localhost:27017$(RESET)"
	@echo "$(YELLOW)Mongo Express: http://localhost:8081 (admin/admin123)$(RESET)"

.PHONY: dev-down
dev-down: ## Stop development services
	@echo "$(BOLD)$(RED)Stopping development services...$(RESET)"
	$(DOCKER_COMPOSE) stop mongodb mongo-express
	@echo "$(BOLD)$(GREEN)Development services stopped!$(RESET)"

.PHONY: status
status: ## Show status of all services
	@echo "$(BOLD)$(CYAN)Service Status:$(RESET)"
	$(DOCKER_COMPOSE) ps

.PHONY: health
health: ## Check health of running services
	@echo "$(BOLD)$(CYAN)Health Check:$(RESET)"
	@echo "$(YELLOW)Checking API health...$(RESET)"
	@curl -f http://localhost:8080/actuator/health 2>/dev/null && echo " $(GREEN)API is healthy$(RESET)" || echo " $(RED)API is down$(RESET)"
	@echo "$(YELLOW)Checking MongoDB...$(RESET)"
	@docker exec billing-discounts-mongo mongosh --eval "db.adminCommand('ping')" --quiet 2>/dev/null && echo " $(GREEN)MongoDB is healthy$(RESET)" || echo " $(RED)MongoDB is down$(RESET)"
	@echo "$(YELLOW)Checking Mongo Express...$(RESET)"
	@curl -f http://localhost:8081 2>/dev/null >/dev/null && echo " $(GREEN)Mongo Express is healthy$(RESET)" || echo " $(RED)Mongo Express is down$(RESET)"

.PHONY: clean-docker
clean-docker: ## Clean up Docker resources
	@echo "$(BOLD)$(RED)Cleaning Docker resources...$(RESET)"
	$(DOCKER_COMPOSE) down -v --remove-orphans
	docker system prune -f
	@echo "$(BOLD)$(GREEN)Docker cleanup completed!$(RESET)"

.PHONY: reset
reset: clean-docker dev-setup ## Reset entire development environment

.PHONY: api-docs
api-docs: ## Open API documentation
	@echo "$(BOLD)$(CYAN)Opening API documentation...$(RESET)"
	@echo "$(YELLOW)Swagger UI: http://localhost:8080/swagger-ui.html$(RESET)"
	@echo "$(YELLOW)API Docs: http://localhost:8080/v3/api-docs$(RESET)"

.PHONY: mongo-express
mongo-express: ## Open Mongo Express in browser
	@echo "$(BOLD)$(CYAN)Opening Mongo Express...$(RESET)"
	@echo "$(YELLOW)URL: http://localhost:8081$(RESET)"
	@echo "$(YELLOW)Username: admin$(RESET)"
	@echo "$(YELLOW)Password: admin123$(RESET)"

.PHONY: all
all: clean build test-coverage docker-build ## Run complete CI pipeline

# Default target
.DEFAULT_GOAL := help