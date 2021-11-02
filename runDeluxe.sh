for i in {1..100}
do
     http post localhost:8081/sales customerId=123 sku=KEBL400x units=30
     echo
done