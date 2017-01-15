package com.fomdeveloper.planket.injection;


import com.fomdeveloper.planket.PlanketApplication;

/**
 * Created by Fernando on 24/12/2016.
 */

public class MockPlanketApplication extends PlanketApplication {
    @Override
    public MockComponent createComponent() {
        return  DaggerMockComponent.builder().mockAppModule(new MockAppModule(this)).build();
    }
}
