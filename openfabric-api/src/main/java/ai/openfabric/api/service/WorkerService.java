package ai.openfabric.api.service;

import ai.openfabric.api.model.Worker;
import ai.openfabric.api.model.WorkerInfo;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Statistics;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WorkerService {
    CreateContainerResponse createContainer(Worker worker);
    WorkerInfo getContainer(String containerName);
    Page<Worker> listContainers(Pageable page);
    String startContainer(String containerName);
    String stopContainer(String containerName);
    Statistics getContainerStats(String containerName);
}
