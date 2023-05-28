package ai.openfabric.api.service;

import ai.openfabric.api.model.Worker;
import ai.openfabric.api.model.WorkerInfo;
import ai.openfabric.api.repository.WorkerInfoRepository;
import ai.openfabric.api.repository.WorkerRepository;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.Statistics;
import com.github.dockerjava.core.InvocationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class WorkerServiceImpl implements WorkerService{

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private WorkerInfoRepository workerInfoRepository;

    private final DockerClient dockerClient;

    @Autowired
    public WorkerServiceImpl(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
    }

    @Override
    public CreateContainerResponse createContainer(Worker worker){
        try {
            CreateContainerResponse container = dockerClient.createContainerCmd(worker.image).withName(worker.name).exec();
            workerRepository.save(worker);
            return container;
        }catch (NotFoundException e){
            throw new NotFoundException("Image not found for name "+worker.image);
        }
    }

    @Override
    public Page<Worker> listContainers(Pageable page){
        return workerRepository.findAll(page);
    }

    @Override
    public String startContainer(String containerName){
        try {
            dockerClient.startContainerCmd(containerName).exec();
            return "Container started";
        }catch (NotFoundException e){
            throw new NotFoundException("Container not found for name "+containerName);
        }
    }

    @Override
    public String stopContainer(String containerName){
        try {
            dockerClient.stopContainerCmd(containerName).exec();
            return "Container stopped";
        }catch (NotFoundException e){
            throw new NotFoundException("Container not found for name "+containerName);
        }
    }

    @Override
    public Statistics getContainerStats(String containerName){
        //get docker conatiner stats
        InvocationBuilder.AsyncResultCallback<Statistics> callback = new InvocationBuilder.AsyncResultCallback<>();
        dockerClient.statsCmd(containerName).exec(callback);
        Statistics stats = null;
        try {
            stats = callback.awaitResult();
            callback.close();
        } catch (NotFoundException | IOException e) {
            throw new NotFoundException("Container not found for name "+containerName);
        }
        return stats;
    }

    @Override
    public WorkerInfo getContainer(String containerName) {
        try {
            InspectContainerResponse containerInfo = dockerClient.inspectContainerCmd(containerName).exec();
            WorkerInfo workerInfo = new WorkerInfo();
            workerInfo.setName(containerInfo.getName());
            workerInfo.setImageId(containerInfo.getImageId());
            workerInfo.setPlatform(containerInfo.getPlatform());
            workerInfo.setRestartCount(containerInfo.getRestartCount());
            workerInfo.setState(containerInfo.getState().getStatus());
            workerInfoRepository.save(workerInfo);
            return workerInfo;

        } catch (NotFoundException e) {
            throw new NotFoundException("Container not found for name "+containerName);
        }
    }
}
