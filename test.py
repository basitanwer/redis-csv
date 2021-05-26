import redis
import pytest_benchmark
    
# r = redis.Redis(host='localhost', port=6379, db=0)
# pool = redis.ConnectionPool(host='localhost', port=6379, db=0)
r = redis.StrictRedis(host='localhost', port=6379, db=0)
# r = redis.Redis(connection_pool=connection.)
# print(r.hgetall('fake:1'))
# print(r.execute_command("DBSIZE"))

def run():
    r.execute_command("MSQL.QUERY", "SELECT name from table1mil where height>8.9 LIMIT 10")

def test_redis(benchmark):
    benchmark(run)