# eventbus
it's about decoupling and the separation of concerns.

How to use?<br/>
add this tag in your application spring.xml<br/>
step 1:
<bean id="eventBus" class="com.qc.eventbus.spring.SpringEventBus" autowire="byName" init-method="init" destroy-method="destroy"/>
step 2:
use eventbus instance like this
eventbus.publish(DemoEvent event);
event is what you are concerned.
step 3:
add @Subscribe annotation on you service method
@Subscribe(async=false) or @Subscribe(async=true)
public void handleDemoEvent(DemoEvent event){
  
}

async=false the thread is synchronous 
async=ture  the thread is asynchronous
