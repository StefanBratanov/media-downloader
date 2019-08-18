package org.stefata.mediadownloader.scheduler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.stefata.mediadownloader.flow.TvShowFlow;
import org.stefata.mediadownloader.persistence.model.TvShow;
import org.stefata.mediadownloader.persistence.repository.TvShowRepository;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {TvShowFlowRunner.class, SchedulerConfig.class})
class TvShowFlowRunnerTest {

    @MockBean
    private TvShowRepository tvShowRepository;
    @MockBean
    private TvShowFlow tvShowFlow;

    @Autowired
    private TvShowFlowRunner subject;

    @Test
    public void runsTvShowFlow() {
        TvShow gameOfThrones = TvShow.builder().title("Game of thrones").build();
        TvShow strangerThings = TvShow.builder().title("Stranger things").build();

        when(tvShowRepository.findAll()).thenReturn(List.of(gameOfThrones, strangerThings));

        subject.runTvShowFlow();

        verify(tvShowFlow).runFlow(gameOfThrones);
        verify(tvShowFlow).runFlow(strangerThings);

        verifyNoMoreInteractions(tvShowFlow);

    }

}