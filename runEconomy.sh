for i in {1..100}
do
     http post localhost:8081/sales customerId=123 sku=KEBL600 units=30
     echo
done