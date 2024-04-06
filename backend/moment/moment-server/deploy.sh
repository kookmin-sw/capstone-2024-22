ssh -tt -p ${SERVER_PORT} ${SERVER_USER}@${SERVER_HOST} -i key.txt -o StrictHostKeyChecking=no << 'ENDSSH'
cd capstone-2024-22/backend/moment/moment-server
# Check if the container is already running
if [ "$(docker ps -q -f name=moment-server)" ]; then
    # If the container is running, stop and remove it
    docker stop moment-server
    docker rm moment-server
fi

# Pull the latest changes from the repository
git pull origin "deploy/v1"

# Build and start the container
docker-compose up --build -d
ENDSSH