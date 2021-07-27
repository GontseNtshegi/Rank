import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'players',
        data: { pageTitle: 'rankInteractiveApp.players.home.title' },
        loadChildren: () => import('./players/players.module').then(m => m.PlayersModule),
      },
      {
        path: 'players-audit',
        data: { pageTitle: 'rankInteractiveApp.playersAudit.home.title' },
        loadChildren: () => import('./players-audit/players-audit.module').then(m => m.PlayersAuditModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
