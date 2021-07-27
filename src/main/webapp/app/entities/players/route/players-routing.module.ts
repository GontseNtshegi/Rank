import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PlayersComponent } from '../list/players.component';
import { PlayersDetailComponent } from '../detail/players-detail.component';
import { PlayersUpdateComponent } from '../update/players-update.component';
import { PlayersRoutingResolveService } from './players-routing-resolve.service';

const playersRoute: Routes = [
  {
    path: '',
    component: PlayersComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PlayersDetailComponent,
    resolve: {
      players: PlayersRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PlayersUpdateComponent,
    resolve: {
      players: PlayersRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PlayersUpdateComponent,
    resolve: {
      players: PlayersRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(playersRoute)],
  exports: [RouterModule],
})
export class PlayersRoutingModule {}
