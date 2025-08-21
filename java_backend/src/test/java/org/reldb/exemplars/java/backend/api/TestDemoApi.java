package org.reldb.exemplars.java.backend.api;

import static org.assertj.core.api.Assertions.assertThat;

import org.reldb.exemplars.java.backend.api.model.DemoIn;
import org.reldb.exemplars.java.backend.api.model.DemoOut;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import org.assertj.core.api.AssertionsForClassTypes;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class TestDemoApi extends ApiTestBase {

    private static final String DEMO_URL = "/demo";

    @Test
    void verifyGetAllDemos() {
        final var demos = getAllDemos();

        AssertionsForInterfaceTypes.assertThat(demos).size().isEqualTo(2);
    }

    @Test
    void canAddUpdateAndRemoveDemo() throws JsonProcessingException {
        final var beforeDemos = getAllDemos();
        AssertionsForInterfaceTypes.assertThat(beforeDemos).size().isEqualTo(2);

        final var newdemo = addDemo("Dave", "Here");

        final var demosAfterAdd = getAllDemos();
        AssertionsForInterfaceTypes.assertThat(demosAfterAdd).hasSize(beforeDemos.size() + 1);

        final var updatedName = "Bob";
        final var updatedAddress = "There";

        updateDemo(newdemo.demoId(), updatedName, updatedAddress);

        final var demoAfterUpdate = getDemo(newdemo.demoId());

        AssertionsForClassTypes.assertThat(demoAfterUpdate.demoId()).isEqualTo(newdemo.demoId());
        AssertionsForClassTypes.assertThat(demoAfterUpdate.name()).isEqualTo(updatedName);
        AssertionsForClassTypes.assertThat(demoAfterUpdate.address()).isEqualTo(updatedAddress);

        deleteDemo(newdemo.demoId());

        final var demosAfterDelete = getAllDemos();
        AssertionsForInterfaceTypes.assertThat(demosAfterDelete).hasSameSizeAs(beforeDemos);
    }

    private List<DemoOut> getAllDemos() {
        final var url = getBaseUrl() + DEMO_URL;

        final ResponseEntity<@NonNull List<DemoOut>> response = get(url, new ParameterizedTypeReference<>() {
        });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().get(X_REQUEST_ID)).isNotNull();
        return response.getBody();
    }

    private DemoOut getDemo(long demoId) {
        final var url = getBaseUrl() + DEMO_URL + "/" + demoId;

        final ResponseEntity<@NonNull DemoOut> response = get(url, new ParameterizedTypeReference<>() {
        });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().get(X_REQUEST_ID)).isNotNull();
        return response.getBody();
    }

    private DemoOut addDemo(String name, String address) throws JsonProcessingException {
        final var url = getBaseUrl() + DEMO_URL;

        final ResponseEntity<@NonNull DemoOut> addResponse = post(url,
                new ParameterizedTypeReference<>() {
                },
                new DemoIn(name, address));

        assertThat(addResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        return addResponse.getBody();
    }

    private void updateDemo(long demoId, String name, String address) throws JsonProcessingException {
        final var url = getBaseUrl() + DEMO_URL + "/" + demoId;

        final ResponseEntity<@NonNull Void> updateResponse = put(url,
                new ParameterizedTypeReference<>() {
                },
                new DemoIn(name, address));

        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private void deleteDemo(long demoId) {
        final var url = getBaseUrl() + DEMO_URL + "/" + demoId;

        final ResponseEntity<@NonNull Void> updateResponse = delete(url, new ParameterizedTypeReference<>() {
        });

        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
