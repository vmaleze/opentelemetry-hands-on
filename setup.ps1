# Colors
$NC = "`e[0m"     # No Color
$Red = "`e[0;31m" # Red
$Green = "`e[0;32m" # Green
$White = "`e[0;37m" # White
$Yellow = "`e[0;33m" # Yellow

Write-Host "${White}------- Check environment --------${NC}"

function Test-Command($cmd) {
    $null = Get-Command $cmd -ErrorAction SilentlyContinue
    return $?
}

if (-not (Test-Command "mise")) {
    Write-Host "${Red}KO${NC}. Please install mise before proceeding"
    exit 1
}
Write-Host "mise: ${Green}OK${NC}"

if (-not (Test-Command "docker")) {
    Write-Host "${Red}KO${NC}. Please install docker before proceeding"
    exit 1
}
Write-Host "docker: ${Green}OK${NC}"

Write-Host "${White}------- Installing necessary tools with mise --------${NC}"

# Equivalent to: mise install
& mise install

Write-Host "${White}------- Setup local k3d cluster --------${NC}"

# Cluster
# & k3d cluster delete otel-hands-on
& k3d cluster create otel-hands-on --agents 2 --port "80:80@loadbalancer" --port "443:443@loadbalancer"
& kubectl create namespace microservices
& kubens microservices

# Kafka
& kubectl apply -n microservices -f microservices/infra/kafka-deployment.yaml

# SigNoz
& helm repo add signoz https://charts.signoz.io
& helm repo update

# Microservices
& helm dependency up ./microservices/order/infra;  & helm upgrade --install order ./microservices/order/infra
& helm dependency up ./microservices/product/infra; & helm upgrade --install product ./microservices/product/infra
& helm dependency up ./microservices/shopping-cart/infra; & helm upgrade --install shopping-cart ./microservices/shopping-cart/infra
& helm dependency up ./microservices/stock/infra; & helm upgrade --install stock ./microservices/stock/infra
