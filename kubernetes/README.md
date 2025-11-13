# Kubernetes Setup for Minikube

## Prerequisites

- Docker installed
- Minikube installed and running (`minikube start`)

## Deploy

Run with bash:

```bash
bash deploy.sh
```

The script builds the image, loads it into minikube, and deploys everything.

## Access

**Port-forward (recommended, uses port 8080):**

Run in one terminal:
```bash
bash port-forward.sh
```

Or manually:
```bash
minikube kubectl -- port-forward -n eurail-zoo service/eurail-app 8080:8080
```

Keep it running, then in another terminal:
```bash
curl http://localhost:8080/api/animals
```

**Or use minikube service:**
```bash
minikube service eurail-app -n eurail-zoo
```
This opens a random port - check the output for the URL.

## Cleanup

**Delete everything:**
```bash
minikube kubectl -- delete namespace eurail-zoo
```

**Or just restart the app (keeps database):**
```bash
minikube kubectl -- delete deployment eurail-app -n eurail-zoo
# Then rerun: bash deploy.sh
```

## Files

- `postgres.yaml` - Database setup
- `app.yaml` - Application deployment
- `deploy.sh` - Deployment script
