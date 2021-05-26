package basit; /* basit created on 22/05/2021 inside the package - basit */

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.commands.ProtocolCommand;
import redis.clients.jedis.util.SafeEncoder;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class RedisSQL {

  private Jedis jedis;

  public static void main(String[] args) throws RunnerException {

    System.out.println("starting");
    Options opt = new OptionsBuilder()
        .include(RedisSQL.class.getSimpleName())
        .forks(1)
        .build();

    new Runner(opt).run();
  }

  @Setup
  public void setup(){
    jedis = new Jedis("localhost", 6379);
    jedis.connect();
  }

  @Benchmark
  public void testQuery(Blackhole blackhole){
    jedis.getClient().sendCommand(ModuleCommand.MSQL,
                                  "SELECT name from t where height>8.9 LIMIT 2");
    var reply = jedis.getClient().getBinaryMultiBulkReply();
    blackhole.consume(reply);
    // for (var obj : reply.toArray()) {
    //   var item = (ArrayList) obj;
    //   for (Object o : item) {
    //     SafeEncoder.encode((byte[]) o);
    //   }
    // }
  }

  @Benchmark
  public void testHGet(Blackhole blackhole){
    Map<String, String> asdf = jedis.hgetAll("fake:1");
    blackhole.consume(asdf);
  }

  enum ModuleCommand implements ProtocolCommand {
    MSQL("MSQL.QUERY");

    private final byte[] raw;

    ModuleCommand(String alt) {
      raw = SafeEncoder.encode(alt);
    }

    @Override
    public byte[] getRaw() {
      return raw;
    }
  }

}
