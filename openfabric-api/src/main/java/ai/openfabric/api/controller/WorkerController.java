package ai.openfabric.api.controller;

import ai.openfabric.api.model.Worker;
import ai.openfabric.api.service.WorkerService;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${node.api.path}/worker")
public class WorkerController {

    @Autowired
    private WorkerService workerService;

    @GetMapping(path="/containers")
    public Page<Worker> getAllContainers(Pageable page){
        return workerService.listContainers(page);
    }

    @ResponseStatus(value= HttpStatus.CREATED)
    @PostMapping(path = "/create")
    public CreateContainerResponse createContainer(@RequestBody Worker worker) {
        return workerService.createContainer(worker);
    }

    @PutMapping(path = "/start/container")
    public String startContainer(@RequestParam("name") String containerName) {
        return workerService.startContainer(containerName);
    }

    @PutMapping(path = "/stop/container")
    public String stopContainer(@RequestParam("name") String containerName) {
        return workerService.stopContainer(containerName);
    }

    @GetMapping(path = "/stats/{containerName}")
    public Statistics getContainerStats(@PathVariable("containerName") String containerName) {
        return workerService.getContainerStats(containerName);
    }


}
