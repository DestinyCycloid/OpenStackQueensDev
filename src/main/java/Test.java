import java.util.List;

import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.openstack.OSFactory;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.model.compute.Flavor;
import org.openstack4j.model.identity.v3.Project;
import org.openstack4j.model.identity.v3.User;

public class Test {
    public static void main(String[] args) {
        OSClientV3 os = OSFactory.builderV3()
                .endpoint("http://192.168.88.132/identity/v3")
                .credentials("admin", "nomoresecret", Identifier.byId("default"))
                .scopeToProject(Identifier.byName("admin"), Identifier.byId("default"))
                .authenticate();
        List<? extends User> users = os.identity().users().list();
        System.out.println(users.size());

        List<? extends Project> projectList = os.identity().projects().list();
        System.out.println(projectList.size());

        List<? extends Flavor> flavors = os.compute().flavors().list();
        System.out.println(flavors.size());

    }
}