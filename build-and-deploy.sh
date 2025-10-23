#!/bin/sh

GREEN=$'\e[0;32m'
RED=$'\e[0;31m'
NC=$'\e[0m'

# build backend
cd ./profile-manager-backend

echo
echo -------------------------------------------------------
echo "Running unit tests..."
echo -------------------------------------------------------
echo
./mvnw clean test
if [[ "$?" -ne 0 ]] ; then
  echo
  echo "❌ ${RED}Failure occurred during unit tests...${NC}"; exit $rc
  echo
else
  echo
  echo "✅ ${GREEN}Unit tests passed!${NC}"
  echo
fi

echo
echo -------------------------------------------------------
echo "Running integration tests..."
echo -------------------------------------------------------
echo
./mvnw clean verify
if [[ "$?" -ne 0 ]] ; then
  echo
  echo "❌ ${RED}Failure occurred during integration tests...${NC}"; exit $rc
  echo
else
  echo
  echo "✅ ${GREEN}Integration tests passed!${NC}"
  echo
fi

# build with docker-compose
cd ../
echo
echo -------------------------------------------------------
echo "🚀 Running docker-compose"
echo -------------------------------------------------------
echo
docker-compose up -d
