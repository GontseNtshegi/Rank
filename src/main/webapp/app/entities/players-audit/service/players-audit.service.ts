import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPlayersAudit, getPlayersAuditIdentifier } from '../players-audit.model';

export type EntityResponseType = HttpResponse<IPlayersAudit>;
export type EntityArrayResponseType = HttpResponse<IPlayersAudit[]>;

@Injectable({ providedIn: 'root' })
export class PlayersAuditService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/players-audits');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(playersAudit: IPlayersAudit): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(playersAudit);
    return this.http
      .post<IPlayersAudit>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(playersAudit: IPlayersAudit): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(playersAudit);
    return this.http
      .put<IPlayersAudit>(`${this.resourceUrl}/${getPlayersAuditIdentifier(playersAudit) as string}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(playersAudit: IPlayersAudit): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(playersAudit);
    return this.http
      .patch<IPlayersAudit>(`${this.resourceUrl}/${getPlayersAuditIdentifier(playersAudit) as string}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<IPlayersAudit>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPlayersAudit[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPlayersAuditToCollectionIfMissing(
    playersAuditCollection: IPlayersAudit[],
    ...playersAuditsToCheck: (IPlayersAudit | null | undefined)[]
  ): IPlayersAudit[] {
    const playersAudits: IPlayersAudit[] = playersAuditsToCheck.filter(isPresent);
    if (playersAudits.length > 0) {
      const playersAuditCollectionIdentifiers = playersAuditCollection.map(
        playersAuditItem => getPlayersAuditIdentifier(playersAuditItem)!
      );
      const playersAuditsToAdd = playersAudits.filter(playersAuditItem => {
        const playersAuditIdentifier = getPlayersAuditIdentifier(playersAuditItem);
        if (playersAuditIdentifier == null || playersAuditCollectionIdentifiers.includes(playersAuditIdentifier)) {
          return false;
        }
        playersAuditCollectionIdentifiers.push(playersAuditIdentifier);
        return true;
      });
      return [...playersAuditsToAdd, ...playersAuditCollection];
    }
    return playersAuditCollection;
  }

  protected convertDateFromClient(playersAudit: IPlayersAudit): IPlayersAudit {
    return Object.assign({}, playersAudit, {
      eventDate: playersAudit.eventDate?.isValid() ? playersAudit.eventDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.eventDate = res.body.eventDate ? dayjs(res.body.eventDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((playersAudit: IPlayersAudit) => {
        playersAudit.eventDate = playersAudit.eventDate ? dayjs(playersAudit.eventDate) : undefined;
      });
    }
    return res;
  }
}
