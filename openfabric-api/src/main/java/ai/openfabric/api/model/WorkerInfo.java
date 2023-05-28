package ai.openfabric.api.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;



@Entity
@Table(name = "worker_info")
public class WorkerInfo extends Datable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "of-uuid")
    @GenericGenerator(name = "of-uuid", strategy = "ai.openfabric.api.model.IDGenerator")
    @Getter
    @Setter
    public String id;

    @Getter
    @Setter
    public String name;

    @Getter
    @Setter
    public String imageId;

    @Getter
    @Setter
    public String state;

    @Getter
    @Setter
    public String platform;

    @Getter
    @Setter
    public int restartCount;


}
