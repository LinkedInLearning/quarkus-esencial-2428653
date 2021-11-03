for i in {1..100}
do
     http post 192.168.99.104:30338/sales customerId=123 sku=KEBL600 units=30
     echo
done