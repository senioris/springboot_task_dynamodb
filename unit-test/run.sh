#!/bin/sh
cd /workspace/local-template
sh deploy.sh
cd /workspace
sh gradlew test
