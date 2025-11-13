#!/bin/bash
minikube kubectl -- port-forward -n eurail-zoo service/eurail-app 8080:8080

