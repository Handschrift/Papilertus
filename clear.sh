./stop.sh
echo "Pruning system..."
docker system prune -a -f --volumes
echo "Pruned system"