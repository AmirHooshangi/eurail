#!/bin/bash
set -e

if ! command -v docker &> /dev/null; then
    echo "Docker not found. Please install Docker first."
    exit 1
fi

if ! command -v minikube &> /dev/null; then
    echo "Minikube not found. Please install minikube first."
    exit 1
fi

# Use minikube kubectl if available, otherwise regular kubectl
if command -v kubectl &> /dev/null; then
    KUBECTL=kubectl
elif minikube kubectl -- version &> /dev/null; then
    KUBECTL="minikube kubectl --"
else
    echo "kubectl not found. Please install kubectl or use minikube."
    exit 1
fi

echo "Building image..."
docker build -t eurail-app:latest ..

echo "Loading image into minikube..."
minikube image load eurail-app:latest

echo "Deploying..."
$KUBECTL apply -f postgres.yaml
$KUBECTL apply -f app.yaml

echo "Waiting for pods..."
$KUBECTL wait --for=condition=ready pod -l app=postgres -n eurail-zoo --timeout=60s || true
sleep 10
$KUBECTL wait --for=condition=ready pod -l app=eurail-app -n eurail-zoo --timeout=60s || true

echo "Starting port-forward in background..."
$KUBECTL port-forward -n eurail-zoo service/eurail-app 8080:8080 > /dev/null 2>&1 &
PORT_FORWARD_PID=$!
sleep 2

echo "Done! App is available at http://localhost:8080"
echo "Port-forward PID: $PORT_FORWARD_PID (run 'kill $PORT_FORWARD_PID' to stop)"
