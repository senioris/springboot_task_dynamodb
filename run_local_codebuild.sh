#/bin/bash
./unit-test/codebuild_build.sh -i public.ecr.aws/codebuild/amazonlinux2-x86_64-standard:5.0 -a artifacts -b ./unit-test-buildspec.yml -d