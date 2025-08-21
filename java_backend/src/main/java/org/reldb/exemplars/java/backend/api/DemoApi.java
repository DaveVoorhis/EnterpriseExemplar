package org.reldb.exemplars.java.backend.api;

import org.reldb.exemplars.java.backend.api.interceptors.users.Permit;
import org.reldb.exemplars.java.backend.api.model.DemoIn;
import org.reldb.exemplars.java.backend.api.model.DemoOut;
import org.reldb.exemplars.java.backend.api.service.DemoServiceAdapter;
import org.reldb.exemplars.java.backend.enums.Permissions;
import java.util.List;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RequestMapping("/demo")
@RestController
public class DemoApi extends ApiDefault {

    @Autowired
    private DemoServiceAdapter demoService;

    @Permit(Permissions.GET_ALL_DEMOS)
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<@NonNull List<DemoOut>> getAll() {
        return ResponseEntity.ok(demoService.getAllDemos());
    }

    @Permit(Permissions.GET_DEMO)
    @GetMapping(value = "{demoId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<@NonNull DemoOut> get(@PathVariable long demoId) {
        return ResponseEntity.ok(demoService.getDemo(demoId));
    }

    @Permit(Permissions.ADD_DEMO)
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<@NonNull DemoOut> add(@RequestBody DemoIn demo) {
        return ResponseEntity.ok(demoService.addDemo(demo));
    }

    @Permit(Permissions.UPDATE_DEMO)
    @PutMapping(value = "/{demoId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<@NonNull Void> update(@PathVariable long demoId, @RequestBody DemoIn demoIn) {
        demoService.updateDemo(demoId, demoIn);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Permit(Permissions.DELETE_DEMO)
    @DeleteMapping(value = "/{demoId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<@NonNull Void> delete(@PathVariable long demoId) {
        demoService.deleteDemo(demoId);
        return ResponseEntity.ok().build();
    }
}