package org.netscan.mvc;

import org.netscan.core.configuration.Configuration;
import org.netscan.core.configuration.ConfigurationUtil;
import org.netscan.core.configuration.Filter;
import org.netscan.core.ipv4.IPv4;
import org.netscan.core.ipv4.IPv4Producer;
import org.netscan.core.ipv4.IPv4Supplier;

import java.nio.file.Paths;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Author Rigoberto Leander Salgado Reyes <rlsalgado2006@gmail.com>
 * <p>
 * Copyright 2015 by Rigoberto Leander Salgado Reyes.
 * <p>
 * This program is licensed to you under the terms of version 3 of the
 * GNU Affero General Public License. This program is distributed WITHOUT
 * ANY EXPRESS OR IMPLIED WARRANTY, INCLUDING THOSE OF NON-INFRINGEMENT,
 * MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE. Please refer to the
 * AGPL (http:www.gnu.org/licenses/agpl-3.0.txt) for more details.
 */
public class Test {

    static final Configuration conf = ConfigurationUtil.loadConfiguration("configuration.json");

    static BlockingQueue<IPv4> queue = new ArrayBlockingQueue<>(10);
    static Filter filter = new Filter("*.*");

    public static void main(String... args) {

        final String s = "smb://10.8.14.19/datos/Documentals/Facebook-66.mp4";

        System.out.println(Paths.get(s).getParent().toString().replace("smb:/", "smb://"));

        System.out.println(System.getenv().get("OSTYPE"));
        System.out.println(System.getProperty("os.name"));


//        final String s = "*.webm, *.mkv, *.flv, *.flv, *.vob, *.ogv, *.ogg, *.drc, *.gif, *.gifv, *.mng, *.avi, *.mov, " +
//                "*.qt, *.wmv, *.yuv, *.rm, *.rmvb, *.asf, *.mp4, *.m4p, *.m4v, *.mpg, *.mp2, *.mpeg, *.mpe, *.mpv, " +
//                "*.mpg, *.mpeg, *.m2v, *.m4v, *.svi, *.3gp, *.3g2, *.mxf, *.roq, *.nsv, *.flv, *.f4v, *.f4p, *.f4a, *.f4b, " +
//                "*.swf, *.mpg4, *.mpeg4, *.dat, *.DAT";
//
//
////        Stream.of(s.replaceAll("\\s*", "").split(",")).forEach(f -> System.out.println(String.format("-%s-", f)));
//
//        final List<String> filters = Arrays.asList(s.replaceAll("\\s*", "").split(","));
////        System.out.println(filters.size());
//        AtomicInteger pos = new AtomicInteger(0);
//        final int size = 10;
//        final String collect =
//                IntStream.generate(() -> pos.getAndAdd(size)).limit(filters.size() / size + (filters.size() % size != 0 ? 1 : 0))
//                        .boxed()
//                        .map(i -> filters.subList(i, i + size > filters.size() ? filters.size() : i + size))
//                        .map(l -> l.stream().collect(Collectors.joining(", ")))
//                        .collect(Collectors.joining(System.getProperty("line.separator")));
//
////
//        System.out.println(collect);

        // final long start = System.currentTimeMillis();
        // startProducer();
        // startConsumer(10);
        // final long end = System.currentTimeMillis();
        //  System.out.println(String.format("time = %d\n",end - start));

        // System.out.println(new IPv4("10.8.14.254").increment());

///scan "hola mundo"
//        ArrayList<Credential> credentials = new ArrayList<>();
//        credentials.add(new Credential("*", "apto14109", "apto14109.."));
//        conf.setCredentials(credentials);
//
//        Filter filter1 = new Filter();
//        filter1.add("*.mpg");
//
//        ArrayList<Filter> filters = new ArrayList<>();
//        filters.add(filter1);
//
//        ArrayList<Range> ranges = new ArrayList<>();
//        ranges.add(new Range(new IPv4("10.8.14.19"), new IPv4("10.8.14.20")));
//
//        conf.setCredentials(credentials);
//        conf.setFilters(filters);
//        conf.setRanges(ranges);
//        conf.setThreads(1);
//
//        NetScanCore netScanCore = new NetScanCore(new IPv4("10.8.14.19"), filter1, credentials);
//
//        netScanCore.produceFiles().stream().forEach(System.out::println);


    }

//    public static void startConsumer(int threads) {
//        IntStream.range(0, threads).forEach(v -> {
//            final IPv4Consumer<IPv4> consumer = new IPv4Consumer<>(queue);
//            final Consumer<IPv4> IPv4Consumer = (ip) -> {
//                System.out.println(ip);
//                NetScanCore core = new NetScanCore(ip, filter, conf.getCredentials(), conf.getTimeOut());
//                final List<Share> shares = core.produceFiles().stream()
//                        .map(sf -> {
//                            try {
//                                return new Share(sf.getName(),
//                                        sf.getCanonicalPath(),
//                                        sf.length(),
//                                        new Date(sf.getDate()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), auth);
//                            } catch (SmbException ignored) {
//                                return null;
//                            }
//                        })
//                        .filter(share -> share != null)
//                        .collect(Collectors.toList());
//
//                // System.out.println(shares.size());
//            };
//
//            new Thread(() -> {
//                while (queue.peek() != null) {
//                    consumer.consume(IPv4Consumer);
//                }
//            }).start();
//        });
//    }

    public static void startProducer() {
        final IPv4Producer<IPv4> producer = new IPv4Producer<>(queue);
        final IPv4Supplier supplier = new IPv4Supplier(conf);

        new Thread(() -> {
            producer.produce(supplier);
        }).start();
    }
}