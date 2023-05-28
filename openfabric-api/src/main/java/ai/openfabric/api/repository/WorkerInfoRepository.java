package ai.openfabric.api.repository;

import ai.openfabric.api.model.WorkerInfo;
import org.springframework.data.repository.CrudRepository;

public interface WorkerInfoRepository extends CrudRepository<WorkerInfo, String> {
}
