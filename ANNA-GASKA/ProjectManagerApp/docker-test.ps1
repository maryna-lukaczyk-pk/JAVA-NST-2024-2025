# PowerShell script to test Docker setup

Write-Host "Building the application..." -ForegroundColor Green
$scriptPath = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $scriptPath
.\mvnw.cmd clean package -DskipTests

Write-Host "Building Docker image and starting containers..." -ForegroundColor Green
docker-compose down
docker-compose up --build -d

Write-Host "Waiting for application to start..." -ForegroundColor Green
Start-Sleep -Seconds 30

Write-Host "Testing application health..." -ForegroundColor Green
$health = Invoke-RestMethod -Uri http://localhost:8080/actuator/health -ErrorAction SilentlyContinue
if ($health.status -eq "UP") {
    Write-Host "Application is healthy!" -ForegroundColor Green
} else {
    Write-Host "Application health check failed!" -ForegroundColor Red
}

Write-Host "Testing application info..." -ForegroundColor Green
try {
    $info = Invoke-RestMethod -Uri http://localhost:8080/actuator/info -ErrorAction SilentlyContinue
    $info | ConvertTo-Json
} catch {
    Write-Host "Could not retrieve application info" -ForegroundColor Yellow
}

Write-Host "To stop the containers, run: docker-compose down" -ForegroundColor Cyan