package PlansTest.service;

import com.example.psoft_22_23_project.plansmanagement.api.CreatePlanRequest;
import com.example.psoft_22_23_project.plansmanagement.api.EditPlansRequest;
import com.example.psoft_22_23_project.plansmanagement.model.*;
import com.example.psoft_22_23_project.plansmanagement.repositories.PlansRepository;
import com.example.psoft_22_23_project.plansmanagement.services.CreatePlansMapper;
import com.example.psoft_22_23_project.plansmanagement.services.PlansServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class PartialUpdateTest {

    @Mock
    private PlansRepository plansRepository;

    @InjectMocks
    private PlansServiceImpl plansService;
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void partialUpdateTest() throws URISyntaxException, IOException, InterruptedException {

        EditPlansRequest request = new EditPlansRequest();
        request.setDescription("New Description");
        request.setActive(true);
        request.setPromoted(true);
        request.setMusicSuggestion("automatic");
        request.setMusicCollection(100);
        request.setNumberOfMinutes("10");
        request.setMaximumNumberOfUsers(10);



        Name name = new Name();
        name.setName("Gold");

        Description description = new Description();
        description.setDescription("Gold Description");

        NumberOfMinutes numberOfMinutes = new NumberOfMinutes();
        numberOfMinutes.setNumberOfMinutes("100");

        MaximumNumberOfUsers maximumNumberOfUsers = new MaximumNumberOfUsers();
        maximumNumberOfUsers.setMaximumNumberOfUsers(5);

        MusicCollection musicCollection = new MusicCollection();
        musicCollection.setMusicCollection(10);

        MusicSuggestion musicSuggestion = new MusicSuggestion();
        musicSuggestion.setMusicSuggestion("personalized");

        AnnualFee annualFee = new AnnualFee();
        annualFee.setAnnualFee(150.00);

        MonthlyFee monthlyFee = new MonthlyFee();
        monthlyFee.setMonthlyFee(15.00);

        Active active = new Active();
        active.setActive(true);

        Promoted promoted = new Promoted();
        promoted.setPromoted(false);

        Plans plan = new Plans(name, description, numberOfMinutes, maximumNumberOfUsers,
                musicCollection, musicSuggestion, annualFee, monthlyFee, active, promoted);


        when(plansRepository.findByName_Name(plan.getName().getName())).thenReturn(Optional.of(plan));
        when(plansRepository.save(Mockito.any(Plans.class))).thenReturn(plan);


        Plans result = plansService.partialUpdate(plan.getName().getName(), request, 0L);


        when(plansRepository.save(Mockito.any(Plans.class))).thenReturn(plan);


        assertNotNull(result);
        assertEquals("Gold", result.getName().getName());
        assertEquals("New Description", result.getDescription().getDescription());
        assertEquals(true, result.getActive().getActive());
        assertEquals(true, result.getPromoted().getPromoted());
        assertEquals("automatic", result.getMusicSuggestion().getMusicSuggestion());
        assertEquals(100, result.getMusicCollection().getMusicCollection());
        assertEquals("10", result.getNumberOfMinutes().getNumberOfMinutes());
        assertEquals(10, result.getMaximumNumberOfUsers().getMaximumNumberOfUsers());

    }

    @Test
    public void promoteTest_PlanDoesNotExist() {
        EditPlansRequest editPlansRequest = new EditPlansRequest();
        String planName = "promoteTest_PlanDoesNotExist";
        assertThrows(IllegalArgumentException.class, () -> plansService.partialUpdate(planName, editPlansRequest,0L));
    }

}
