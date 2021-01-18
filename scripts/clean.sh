#!/bin/bash
# @author: Rubatharisan Thirumathyam
# Cleaning local images (build-local)
BUILD="build-local" docker-compose --project-name production -f docker-compose.yaml down --rmi all
