package PlansTest.service;

import com.example.psoft_22_23_project.plansmanagement.api.EditPlanMoneyRequest;
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
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class MoneyUpdateTest {

    @Mock
    private PlansRepository plansRepository;
    @InjectMocks
    private PlansServiceImpl plansService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void moneyUpdateTest() {

        EditPlanMoneyRequest request = new EditPlanMoneyRequest();

        request.setAnnualFee(10.0);
        request.setMonthlyFee(10.0);

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

        Authentication authentication = new TestingAuthenticationToken("1", "password");
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        Plans result = plansService.moneyUpdate(plan.getName().getName(), request, 0L);


        when(plansRepository.save(Mockito.any(Plans.class))).thenReturn(plan);


        assertNotNull(result);
        assertEquals(10.0, result.getAnnualFee().getAnnualFee());
        assertEquals(10.0, result.getMonthlyFee().getMonthlyFee());


    }

    @Test
    public void Test_PlanDoesNotExist() {
        EditPlanMoneyRequest editPlansRequest = new EditPlanMoneyRequest();
        String planName = "Test_PlanDoesNotExist";
        assertThrows(IllegalArgumentException.class, () -> plansService.moneyUpdate(planName, editPlansRequest,0L));
    }

}
