import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.model.compute.*;
import org.openstack4j.model.identity.v3.User;
import org.openstack4j.model.image.Image;
import org.openstack4j.model.network.Network;
import org.openstack4j.model.network.Router;
import org.openstack4j.model.network.Subnet;
import org.openstack4j.openstack.OSFactory;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Test {
    public static void main(String[] args) {
        OSClientV3 os = OSFactory.builderV3()
                .endpoint("http://192.168.88.132/identity/v3")
                .credentials("admin", "nomoresecret", Identifier.byId("default"))
                .scopeToProject(Identifier.byName("admin"), Identifier.byId("default"))
                .authenticate();

        // Find all Compute Flavors
        List<? extends Flavor> flavors = os.compute().flavors().list();
        System.out.println(flavors);

        // List all Networks
        List<? extends Network> networks = os.networking().network().list();
        System.out.println(networks);

        // List all Subnets
        List<? extends Subnet> subnets = os.networking().subnet().list();
        System.out.println(subnets);

        // List all Routers
        List<? extends Router> routers = os.networking().router().list();
        System.out.println(routers);

//        File file = null;
//        try {
//            file = new File(new URI("file:///e:/temp"));
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//
//        ActionResponse download = os.imagesV2().download("989ecfd2-c58b-4775-9462-f4719afae58d", file);

        Flavor flavor = Builders.flavor()
                .name("test")
                .id("test")
                .ram(4096)
                .vcpus(4)
                .disk(140)
                .build();

        flavor = os.compute().flavors().create(flavor);

        // Create a Server Model Object
        ServerCreate sc = Builders.server().name("demo").flavor("test").image("989ecfd2-c58b-4775-9462-f4719afae58d").build();

        // Boot the Server
        Server server = os.compute().servers().boot(sc);
        os.compute().servers().waitForServerStatus(server.getId(), Server.Status.ACTIVE, 80000, TimeUnit.MILLISECONDS);

        VNCConsole novnc = os.compute().servers().getVNCConsole(server.getId(), VNCConsole.Type.NOVNC);
        System.out.println(novnc);

        os.compute().flavors().delete(flavor.getId());

        os.compute().servers().delete(server.getId());
    }
}