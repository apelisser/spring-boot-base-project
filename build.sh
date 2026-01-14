#!/bin/bash

set -e

APP_NAME="app-base-project"

DO_BUILD=true
START_CONTAINERS=false
STOP_CONTAINERS=false
RESTART_CONTAINERS=false

usage() {
    echo ""
    echo "Usage: ./build.sh [options]"
    echo ""
    echo "Options:"
    echo "  -b, --build     Build the Docker image only (default)"
    echo "  -u, --up        Build the image and start the containers"
    echo "  -r, --restart   Restart the environment (down + build + up)"
    echo "  -d, --down      Stop and remove the containers"
    echo "  -h, --help      Show this help message"
}

# Argument parsing
while [[ $# -gt 0 ]]; do
    case "$1" in
        -u|--up)
            START_CONTAINERS=true
            shift
            ;;
        -b|--build)
            DO_BUILD=true
            shift
            ;;
        -r|--restart)
            RESTART_CONTAINERS=true
            START_CONTAINERS=true
            DO_BUILD=true
            shift
            ;;
        -d|--down)
            STOP_CONTAINERS=true
            DO_BUILD=false
            START_CONTAINERS=false
            shift
            ;;
        -h|--help)
            usage
            exit 0
            ;;
        *)
            echo "Unknown option: $1"
            usage
            exit 1
            ;;
    esac
done

echo "========================================="
echo " Base Project - Build & Local Runtime"
echo "========================================="

# DOWN has top priority
if [ "$STOP_CONTAINERS" = true ]; then
    echo ""
    echo "Stopping environment using Docker Compose..."
    docker compose down -v
    echo "Environment stopped."
    exit 0
fi

# Ensure mvnw is executable (local environments)
if [ ! -x "./mvnw" ]; then
    echo "Maven Wrapper is not executable. Fixing permissions..."
    chmod +x ./mvnw
fi

echo "Extracting version from pom.xml..."
APP_VERSION=$(./mvnw -q \
    -Dexec.executable=echo \
    -Dexec.args='${project.version}' \
    --non-recursive exec:exec)

if [ -z "$APP_VERSION" ]; then
    echo "Failed to obtain version from pom.xml"
    exit 1
fi

echo "Detected version: $APP_VERSION"

echo "Generating .env file..."
echo "APP_VERSION=$APP_VERSION" > .env

if [ "$RESTART_CONTAINERS" = true ]; then
    echo ""
    echo "Restarting environment..."
    docker compose down
fi

if [ "$DO_BUILD" = true ]; then
    echo ""
    echo "Building Docker image..."
    docker build -t "$APP_NAME:$APP_VERSION" .
fi

if [ "$START_CONTAINERS" = true ]; then
    echo ""
    echo "Starting environment with Docker Compose..."
    docker compose up -d --no-build
    echo ""
    echo "======================================"
    echo " Environment is up (version $APP_VERSION)"
    echo " Swagger UI:"
    echo " http://localhost/base-app/swagger-ui/index.html"
    echo "======================================"
else
    echo ""
    echo "Build completed."
    echo "Containers were NOT started."
    echo "To start the environment, run:"
    echo "  ./build.sh --up"
fi
