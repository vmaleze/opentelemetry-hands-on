#!/bin/bash

NC='\033[0m' # No Color
Red='\033[0;31m' # Red
Green='\033[0;32m' # Green
White='\033[0;37m' # White
Yellow='\033[0;33m' # Yellow

echo -e "${White}------- Check environment --------${NC}"

if ! command -v mise &> /dev/null
then
    echo -e "${Red}KO${NC}. Please install mise before proceeding"
    exit
fi
echo -e "mise: ${Green}OK${NC}"
if ! command -v docker &> /dev/null
then
    echo -e "${Red}KO${NC}. Please install docker before proceeding"
    exit
fi
echo -e "docker: ${Green}OK${NC}"

echo -e "${White}------- Installing necessary tools with mise --------${NC}"

mise install

echo -e "${White}------- Setup local k3d cluster --------${NC}"

### Cluster
#k3d cluster delete otel-hands-on
k3d cluster create otel-hands-on --agents 2 --port "80:80@loadbalancer" --port "443:443@loadbalancer"
kubectl create namespace microservices
kubens microservices

### Kafka
kubectl apply -n microservices -f microservices/infra/kafka-deployment.yaml

### SigNoz
helm repo add signoz https://charts.signoz.io
helm repo update

### Microservices
helm dependency up ./microservices/order/infra && helm upgrade --install order ./microservices/order/infra
helm dependency up ./microservices/product/infra && helm upgrade --install product ./microservices/product/infra
helm dependency up ./microservices/shopping-cart/infra && helm upgrade --install shopping-cart ./microservices/shopping-cart/infra
helm dependency up ./microservices/stock/infra && helm upgrade --install stock ./microservices/stock/infra
