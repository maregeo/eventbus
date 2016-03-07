# eventbus
it's about decoupling and the separation of concerns.

How to use?<br/>
add this tag in your application spring.xml<br/>
step 1:<br/>
<![CDATA[ <bean id="eventBus" class="com.qc.eventbus.spring.SpringEventBus" autowire="byName" init-method="init" destroy-method="destroy"/>]]><br/>
step 2:<br/>
use eventbus instance like this<br/>
eventbus.publish(DemoEvent event);<br/>
event is what you are concerned.<br/>
step 3:<br/>
add @Subscribe annotation on you service method<br/>
@Subscribe(async=false) or @Subscribe(async=true)<br/>
public void handleDemoEvent(DemoEvent event){<br/>
  <br/>
}<br/>
<br/>
async=false the thread is synchronous <br/>
async=ture  the thread is asynchronous<br/>
