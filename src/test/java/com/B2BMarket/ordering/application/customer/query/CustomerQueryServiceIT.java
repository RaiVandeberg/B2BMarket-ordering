package com.B2BMarket.ordering.application.customer.query;

import com.B2BMarket.ordering.domain.model.commons.Email;
import com.B2BMarket.ordering.domain.model.commons.FullName;
import com.B2BMarket.ordering.domain.model.customer.Customer;
import com.B2BMarket.ordering.domain.model.customer.CustomerId;
import com.B2BMarket.ordering.domain.model.customer.CustomerTestDataBuilder;
import com.B2BMarket.ordering.domain.model.customer.Customers;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@SpringBootTest
@Transactional
class CustomerQueryServiceIT {

    @Autowired
    private CustomerQueryService queryService;

    @Autowired
    private Customers customers;

    @Test
    public void shouldFilterByFirstNameIgnoringCase() {
        Customer john = addCustomer("John", "john.doe@email.com");
        Customer johnny = addCustomer("Johnny", "johnny.bravo@email.com");
        addCustomer("Mary", "mary.jane@email.com");

        CustomerFilter filter = new CustomerFilter();
        filter.setFirstName("JOHN");

        Page<CustomerSummaryOutput> page = queryService.filter(filter);

        Assertions.assertThat(page.getTotalElements()).isEqualTo(2);
        Assertions.assertThat(page.getContent())
                .extracting(CustomerSummaryOutput::getId)
                .containsExactlyInAnyOrder(john.id().value(), johnny.id().value());
    }

    @Test
    public void shouldFilterByPartOfFirstName() {
        Customer john = addCustomer("John", "john.doe@email.com");
        addCustomer("Mary", "mary.jane@email.com");

        CustomerFilter filter = new CustomerFilter();
        filter.setFirstName("oh");

        Page<CustomerSummaryOutput> page = queryService.filter(filter);

        Assertions.assertThat(page.getContent())
                .extracting(CustomerSummaryOutput::getId)
                .containsExactly(john.id().value());
    }

    @Test
    public void shouldFilterByEmailIgnoringCase() {
        Customer johnDoe = addCustomer("John", "john.doe@email.com");
        Customer maryJohn = addCustomer("Mary", "mary.john@email.com");
        addCustomer("Peter", "peter.parker@email.com");

        CustomerFilter filter = new CustomerFilter();
        filter.setEmail("JOHN");

        Page<CustomerSummaryOutput> page = queryService.filter(filter);

        Assertions.assertThat(page.getTotalElements()).isEqualTo(2);
        Assertions.assertThat(page.getContent())
                .extracting(CustomerSummaryOutput::getId)
                .containsExactlyInAnyOrder(johnDoe.id().value(), maryJohn.id().value());
    }

    @Test
    public void shouldFilterByFirstNameAndEmail() {
        Customer johnWithJohnEmail = addCustomer("John", "john.doe@email.com");
        addCustomer("John", "jdoe@company.com");
        addCustomer("Mary", "mary.john@email.com");

        CustomerFilter filter = new CustomerFilter();
        filter.setFirstName("john");
        filter.setEmail("john");

        Page<CustomerSummaryOutput> page = queryService.filter(filter);

        Assertions.assertThat(page.getTotalElements()).isEqualTo(1);
        Assertions.assertThat(page.getContent())
                .extracting(CustomerSummaryOutput::getId)
                .containsExactly(johnWithJohnEmail.id().value());
    }

    @Test
    public void shouldFilterByPage() {
        addFiveCustomers();

        Page<CustomerSummaryOutput> page = queryService.filter(new CustomerFilter(2, 0));

        Assertions.assertThat(page.getTotalPages()).isEqualTo(3);
        Assertions.assertThat(page.getTotalElements()).isEqualTo(5);
        Assertions.assertThat(page.getNumberOfElements()).isEqualTo(2);
    }

    @Test
    public void shouldReturnRemainingElementsOnLastPage() {
        addFiveCustomers();

        Page<CustomerSummaryOutput> page = queryService.filter(new CustomerFilter(2, 2));

        Assertions.assertThat(page.getTotalPages()).isEqualTo(3);
        Assertions.assertThat(page.getTotalElements()).isEqualTo(5);
        Assertions.assertThat(page.getNumberOfElements()).isEqualTo(1);
        Assertions.assertThat(page.isLast()).isTrue();
    }

    @Test
    public void shouldOrderByRegisteredAtAscByDefault() {
        OffsetDateTime now = OffsetDateTime.now();
        Customer charlie = addCustomer("Charlie", "charlie@email.com", now.minusDays(3));
        Customer alice = addCustomer("Alice", "alice@email.com", now.minusDays(2));
        Customer bob = addCustomer("Bob", "bob@email.com", now.minusDays(1));

        Page<CustomerSummaryOutput> page = queryService.filter(new CustomerFilter());

        Assertions.assertThat(page.getContent())
                .extracting(CustomerSummaryOutput::getId)
                .containsExactly(charlie.id().value(), alice.id().value(), bob.id().value());
    }

    @Test
    public void shouldOrderByRegisteredAtDesc() {
        OffsetDateTime now = OffsetDateTime.now();
        Customer charlie = addCustomer("Charlie", "charlie@email.com", now.minusDays(3));
        Customer alice = addCustomer("Alice", "alice@email.com", now.minusDays(2));
        Customer bob = addCustomer("Bob", "bob@email.com", now.minusDays(1));

        CustomerFilter filter = new CustomerFilter();
        filter.setSortByProperty(CustomerFilter.SortType.REGISTERED_AT);
        filter.setSortDirection(Sort.Direction.DESC);

        Page<CustomerSummaryOutput> page = queryService.filter(filter);

        Assertions.assertThat(page.getContent())
                .extracting(CustomerSummaryOutput::getId)
                .containsExactly(bob.id().value(), alice.id().value(), charlie.id().value());
    }

    @Test
    public void shouldOrderByFirstNameAsc() {
        OffsetDateTime now = OffsetDateTime.now();
        Customer charlie = addCustomer("Charlie", "charlie@email.com", now.minusDays(3));
        Customer alice = addCustomer("Alice", "alice@email.com", now.minusDays(2));
        Customer bob = addCustomer("Bob", "bob@email.com", now.minusDays(1));

        CustomerFilter filter = new CustomerFilter();
        filter.setSortByProperty(CustomerFilter.SortType.FIRST_NAME);
        filter.setSortDirection(Sort.Direction.ASC);

        Page<CustomerSummaryOutput> page = queryService.filter(filter);

        Assertions.assertThat(page.getContent())
                .extracting(CustomerSummaryOutput::getFirstName)
                .containsExactly("Alice", "Bob", "Charlie");
        Assertions.assertThat(page.getContent())
                .extracting(CustomerSummaryOutput::getId)
                .containsExactly(alice.id().value(), bob.id().value(), charlie.id().value());
    }

    @Test
    public void shouldOrderByFirstNameDesc() {
        OffsetDateTime now = OffsetDateTime.now();
        addCustomer("Charlie", "charlie@email.com", now.minusDays(3));
        addCustomer("Alice", "alice@email.com", now.minusDays(2));
        addCustomer("Bob", "bob@email.com", now.minusDays(1));

        CustomerFilter filter = new CustomerFilter();
        filter.setSortByProperty(CustomerFilter.SortType.FIRST_NAME);
        filter.setSortDirection(Sort.Direction.DESC);

        Page<CustomerSummaryOutput> page = queryService.filter(filter);

        Assertions.assertThat(page.getContent())
                .extracting(CustomerSummaryOutput::getFirstName)
                .containsExactly("Charlie", "Bob", "Alice");
    }

    @Test
    public void givenNonMatchingFilter_whenFilter_shouldReturnEmptyPage() {
        addCustomer("John", "john.doe@email.com");
        addCustomer("Mary", "mary.jane@email.com");

        CustomerFilter filter = new CustomerFilter();
        filter.setFirstName("Zelda");
        filter.setEmail("zelda");

        Page<CustomerSummaryOutput> page = queryService.filter(filter);

        Assertions.assertThat(page.getTotalPages()).isEqualTo(0);
        Assertions.assertThat(page.getTotalElements()).isEqualTo(0);
        Assertions.assertThat(page.getNumberOfElements()).isEqualTo(0);
        Assertions.assertThat(page.getContent()).isEmpty();
    }

    private void addFiveCustomers() {
        addCustomer("Alice", "alice@email.com");
        addCustomer("Bob", "bob@email.com");
        addCustomer("Charlie", "charlie@email.com");
        addCustomer("Diana", "diana@email.com");
        addCustomer("Edward", "edward@email.com");
    }

    private Customer addCustomer(String firstName, String email) {
        return addCustomer(firstName, email, OffsetDateTime.now());
    }

    private Customer addCustomer(String firstName, String email, OffsetDateTime registeredAt) {
        Customer customer = CustomerTestDataBuilder.existingCustomer()
                .id(new CustomerId())
                .fullName(new FullName(firstName, "Doe"))
                .email(new Email(email))
                .registeredAt(registeredAt)
                .build();
        customers.add(customer);
        return customer;
    }
}
