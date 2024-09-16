import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { LeadListComponent } from './lead/lead-list.component';
import { LeadAddComponent } from './lead/lead-add.component';
import { LeadEditComponent } from './lead/lead-edit.component';
import { UserListComponent } from './user/user-list.component';
import { UserAddComponent } from './user/user-add.component';
import { UserEditComponent } from './user/user-edit.component';
import { CustomerListComponent } from './customer/customer-list.component';
import { CustomerAddComponent } from './customer/customer-add.component';
import { CustomerEditComponent } from './customer/customer-edit.component';
import { SalesOpportunityListComponent } from './sales-opportunity/sales-opportunity-list.component';
import { SalesOpportunityAddComponent } from './sales-opportunity/sales-opportunity-add.component';
import { SalesOpportunityEditComponent } from './sales-opportunity/sales-opportunity-edit.component';
import { TaskListComponent } from './task/task-list.component';
import { TaskAddComponent } from './task/task-add.component';
import { TaskEditComponent } from './task/task-edit.component';
import { ActivityLogListComponent } from './activity-log/activity-log-list.component';
import { ActivityLogAddComponent } from './activity-log/activity-log-add.component';
import { ActivityLogEditComponent } from './activity-log/activity-log-edit.component';
import { AuthenticationComponent } from './security/authentication.component';
import { RegistrationComponent } from './security/registration.component';
import { ErrorComponent } from './error/error.component';
import { AuthenticationService, ADMIN, SALESPERSON } from 'app/security/authentication.service';


export const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    title: $localize`:@@home.index.headline:Welcome to your new app!`,
    data: {
      roles: [ADMIN]
    }
  },
  {
    path: 'leads',
    component: LeadListComponent,
    title: $localize`:@@lead.list.headline:Leads`,
    data: {
      roles: [ADMIN, SALESPERSON]
    }
  },
  {
    path: 'leads/add',
    component: LeadAddComponent,
    title: $localize`:@@lead.add.headline:Add Lead`,
    data: {
      roles: [ADMIN, SALESPERSON]
    }
  },
  {
    path: 'leads/edit/:id',
    component: LeadEditComponent,
    title: $localize`:@@lead.edit.headline:Edit Lead`,
    data: {
      roles: [ADMIN, SALESPERSON]
    }
  },
  {
    path: 'users',
    component: UserListComponent,
    title: $localize`:@@user.list.headline:Users`,
    data: {
      roles: [ADMIN, SALESPERSON]
    }
  },
  {
    path: 'users/add',
    component: UserAddComponent,
    title: $localize`:@@user.add.headline:Add User`,
    data: {
      roles: [ADMIN, SALESPERSON]
    }
  },
  {
    path: 'users/edit/:id',
    component: UserEditComponent,
    title: $localize`:@@user.edit.headline:Edit User`,
    data: {
      roles: [ADMIN, SALESPERSON]
    }
  },
  {
    path: 'customers',
    component: CustomerListComponent,
    title: $localize`:@@customer.list.headline:Customers`,
    data: {
      roles: [ADMIN, SALESPERSON]
    }
  },
  {
    path: 'customers/add',
    component: CustomerAddComponent,
    title: $localize`:@@customer.add.headline:Add Customer`,
    data: {
      roles: [ADMIN, SALESPERSON]
    }
  },
  {
    path: 'customers/edit/:id',
    component: CustomerEditComponent,
    title: $localize`:@@customer.edit.headline:Edit Customer`,
    data: {
      roles: [ADMIN, SALESPERSON]
    }
  },
  {
    path: 'salesOpportunities',
    component: SalesOpportunityListComponent,
    title: $localize`:@@salesOpportunity.list.headline:Sales Opportunities`,
    data: {
      roles: [ADMIN, SALESPERSON]
    }
  },
  {
    path: 'salesOpportunities/add',
    component: SalesOpportunityAddComponent,
    title: $localize`:@@salesOpportunity.add.headline:Add Sales Opportunity`,
    data: {
      roles: [ADMIN, SALESPERSON]
    }
  },
  {
    path: 'salesOpportunities/edit/:id',
    component: SalesOpportunityEditComponent,
    title: $localize`:@@salesOpportunity.edit.headline:Edit Sales Opportunity`,
    data: {
      roles: [ADMIN, SALESPERSON]
    }
  },
  {
    path: 'tasks',
    component: TaskListComponent,
    title: $localize`:@@task.list.headline:Tasks`,
    data: {
      roles: [ADMIN, SALESPERSON]
    }
  },
  {
    path: 'tasks/add',
    component: TaskAddComponent,
    title: $localize`:@@task.add.headline:Add Task`,
    data: {
      roles: [ADMIN, SALESPERSON]
    }
  },
  {
    path: 'tasks/edit/:id',
    component: TaskEditComponent,
    title: $localize`:@@task.edit.headline:Edit Task`,
    data: {
      roles: [ADMIN, SALESPERSON]
    }
  },
  {
    path: 'activityLogs',
    component: ActivityLogListComponent,
    title: $localize`:@@activityLog.list.headline:Activity Logs`,
    data: {
      roles: [ADMIN, SALESPERSON]
    }
  },
  {
    path: 'activityLogs/add',
    component: ActivityLogAddComponent,
    title: $localize`:@@activityLog.add.headline:Add Activity Log`,
    data: {
      roles: [ADMIN, SALESPERSON]
    }
  },
  {
    path: 'activityLogs/edit/:id',
    component: ActivityLogEditComponent,
    title: $localize`:@@activityLog.edit.headline:Edit Activity Log`,
    data: {
      roles: [ADMIN, SALESPERSON]
    }
  },
  {
    path: 'login',
    component: AuthenticationComponent,
    title: $localize`:@@authentication.login.headline:Login`
  },
  {
    path: 'register',
    component: RegistrationComponent,
    title: $localize`:@@registration.register.headline:Registration`
  },
  {
    path: 'error',
    component: ErrorComponent,
    title: $localize`:@@error.headline:Error`
  },
  {
    path: '**',
    component: ErrorComponent,
    title: $localize`:@@notFound.headline:Page not found`
  }
];

// add authentication check to all routes
for (const route of routes) {
  route.canActivate = [(route: ActivatedRouteSnapshot) => inject(AuthenticationService).checkAccessAllowed(route)];
}
