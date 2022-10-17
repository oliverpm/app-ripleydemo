IP=$(kubectl get nodes -o wide | grep minikube |  awk {'print $6'})
PORT=$(kubectl get services | grep app-ripleydemo-service | awk {'print $5'} | awk -F ':' {'print $2'} | awk -F '/' {'print $1'})
echo "http://"$IP":"$PORT
curl http://$IP:$PORT