package devicetests.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.psoft_22_23_project.devicemanagement.model.Device;
import com.example.psoft_22_23_project.devicemanagement.model.MacAddress;
import com.example.psoft_22_23_project.devicemanagement.repositories.DeviceRepository;
import com.example.psoft_22_23_project.plansmanagement.model.*;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.PaymentType;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.Subscriptions;
import com.example.psoft_22_23_project.usermanagement.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class DeviceRepositoryTest {

    @Mock
    private DeviceRepository deviceRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    private Plans createPlan(String name) {
        com.example.psoft_22_23_project.plansmanagement.model.Name planName = new com.example.psoft_22_23_project.plansmanagement.model.Name();
        planName.setName(name);

        com.example.psoft_22_23_project.plansmanagement.model.Description description = new com.example.psoft_22_23_project.plansmanagement.model.Description();
        description.setDescription("Plan Description");

        NumberOfMinutes numberOfMinutes = new NumberOfMinutes();
        numberOfMinutes.setNumberOfMinutes("100");

        MaximumNumberOfUsers maximumNumberOfUsers = new MaximumNumberOfUsers();
        maximumNumberOfUsers.setMaximumNumberOfUsers(5);

        MusicCollection musicCollection = new MusicCollection();
        musicCollection.setMusicCollection(10);

        MusicSuggestion musicSuggestion = new MusicSuggestion();
        musicSuggestion.setMusicSuggestion("personalized");

        AnnualFee annualFee = new AnnualFee();
        annualFee.setAnnualFee(100.00);

        MonthlyFee monthlyFee = new MonthlyFee();
        monthlyFee.setMonthlyFee(10.00);

        Active activeStatus = new Active();
        activeStatus.setActive(true);

        Promoted promotedStatus = new Promoted();
        promotedStatus.setPromoted(false);

        return new Plans(planName, description, numberOfMinutes, maximumNumberOfUsers,
                musicCollection, musicSuggestion, annualFee, monthlyFee, activeStatus, promotedStatus);
    }

    private Subscriptions createSubscription(String plan){

        PaymentType paymentType = new PaymentType("monthly");

        return new Subscriptions(createPlan(plan), paymentType, createUser());

    }

    private User createUser(){

        return new User("tomas@mail.com", "tomaspass");

    }

    private Device createDevice(String macAddresss, String plan){
        // Create test data
        MacAddress macAddress = new MacAddress();
        macAddress.setMacAddress(macAddresss);

        com.example.psoft_22_23_project.devicemanagement.model.Name name = new com.example.psoft_22_23_project.devicemanagement.model.Name();
        name.setName("Device Name");

        com.example.psoft_22_23_project.devicemanagement.model.Description description = new com.example.psoft_22_23_project.devicemanagement.model.Description();
        description.setDescription("Device Description");

        Subscriptions subscription = createSubscription(plan);

        return new Device(macAddress, name, description, subscription);
    }

    @Test
    public void testFindByMacAddress() {
        String macAddress = "00:11:22:33:44:55";
        Device device = createDevice(macAddress, "Plan Name");

        when(deviceRepository.findByMacAddress_MacAddress(macAddress)).thenReturn(Optional.of(device));

        Optional<Device> result = deviceRepository.findByMacAddress_MacAddress(macAddress);

        assertTrue(result.isPresent());
        assertEquals(device, result.get());

        verify(deviceRepository).findByMacAddress_MacAddress(macAddress);
    }

    @Test
    public void testDeleteById() {
        Long deviceId = 1L;

        deviceRepository.deleteById(deviceId);

        verify(deviceRepository).deleteById(deviceId);
    }

    @Test
    public void testDeleteByIdIfMatches() {
        Long deviceId = 1L;
        long desiredVersion = 1L;

        when(deviceRepository.deleteByIdIfMatches(deviceId, desiredVersion)).thenReturn(1);

        int result = deviceRepository.deleteByIdIfMatches(deviceId, desiredVersion);

        assertEquals(1, result);

        verify(deviceRepository).deleteByIdIfMatches(deviceId, desiredVersion);
    }

    @Test
    public void testCountBySubscription() {
        Subscriptions subscription = createSubscription("Plan Name");

        when(deviceRepository.countBySubscription(subscription)).thenReturn(5);

        int result = deviceRepository.countBySubscription(subscription);

        assertEquals(5, result);

        verify(deviceRepository).countBySubscription(subscription);
    }

    @Test
    public void testFindAllBySubscription() {
        Subscriptions subscription = createSubscription("Plan Name");

        List<Device> devices = new ArrayList<>();
        devices.add(createDevice("00:11:22:33:44:55", "Plan Name"));
        devices.add(createDevice("00:11:22:33:44:56", "Plan Name"));
        devices.add(createDevice("00:11:22:33:44:57", "Plan Name"));

        when(deviceRepository.findAllBySubscription(subscription)).thenReturn(devices);

        List<Device> result = deviceRepository.findAllBySubscription(subscription);

        assertEquals(devices, result);

        verify(deviceRepository).findAllBySubscription(subscription);
    }


}
