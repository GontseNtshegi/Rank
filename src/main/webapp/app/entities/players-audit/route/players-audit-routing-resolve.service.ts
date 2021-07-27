import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPlayersAudit, PlayersAudit } from '../players-audit.model';
import { PlayersAuditService } from '../service/players-audit.service';

@Injectable({ providedIn: 'root' })
export class PlayersAuditRoutingResolveService implements Resolve<IPlayersAudit> {
  constructor(protected service: PlayersAuditService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPlayersAudit> | Observable<never> {
    const id = route.params['transactionId'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((playersAudit: HttpResponse<PlayersAudit>) => {
          if (playersAudit.body) {
            return of(playersAudit.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PlayersAudit());
  }
}
