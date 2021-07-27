import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PlayersAuditComponent } from '../list/players-audit.component';
import { PlayersAuditDetailComponent } from '../detail/players-audit-detail.component';
import { PlayersAuditUpdateComponent } from '../update/players-audit-update.component';
import { PlayersAuditRoutingResolveService } from './players-audit-routing-resolve.service';

const playersAuditRoute: Routes = [
  {
    path: '',
    component: PlayersAuditComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':transactionId/view',
    component: PlayersAuditDetailComponent,
    resolve: {
      playersAudit: PlayersAuditRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PlayersAuditUpdateComponent,
    resolve: {
      playersAudit: PlayersAuditRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':transactionId/edit',
    component: PlayersAuditUpdateComponent,
    resolve: {
      playersAudit: PlayersAuditRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(playersAuditRoute)],
  exports: [RouterModule],
})
export class PlayersAuditRoutingModule {}
