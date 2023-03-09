docker login --username ismaellamgadar --password ${{secrets.DOCKER_PASSWORD}}
docker build -t backend .
docker tag backend ismaellamgadar/backend
docker push ismaellamgadar/backend