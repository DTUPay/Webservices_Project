#!/bin/Bash

cat << EOF 
$$$$$$$\  $$$$$$$$\ $$\   $$\       $$$$$$$\   $$$$$$\  $$\     $$\ 
$$  __$$\ \__$$  __|$$ |  $$ |      $$  __$$\ $$  __$$\ \$$\   $$  |
$$ |  $$ |   $$ |   $$ |  $$ |      $$ |  $$ |$$ /  $$ | \$$\ $$  / 
$$ |  $$ |   $$ |   $$ |  $$ |      $$$$$$$  |$$$$$$$$ |  \$$$$  /  
$$ |  $$ |   $$ |   $$ |  $$ |      $$  ____/ $$  __$$ |   \$$  /   
$$ |  $$ |   $$ |   $$ |  $$ |      $$ |      $$ |  $$ |    $$ |    
$$$$$$$  |   $$ |   \$$$$$$  |      $$ |      $$ |  $$ |    $$ |    
\_______/    \__|    \______/       \__|      \__|  \__|    \__|    
EOF

echo "Get into system_tests directory"
cd "$(dirname "$0")"

echo "Building and running integration tests!"
cat docker-compose.yaml
docker-compose pull
docker-compose up
