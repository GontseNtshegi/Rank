jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPlayersAudit, PlayersAudit } from '../players-audit.model';
import { PlayersAuditService } from '../service/players-audit.service';

import { PlayersAuditRoutingResolveService } from './players-audit-routing-resolve.service';

describe('Service Tests', () => {
  describe('PlayersAudit routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: PlayersAuditRoutingResolveService;
    let service: PlayersAuditService;
    let resultPlayersAudit: IPlayersAudit | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(PlayersAuditRoutingResolveService);
      service = TestBed.inject(PlayersAuditService);
      resultPlayersAudit = undefined;
    });

    describe('resolve', () => {
      it('should return IPlayersAudit returned by find', () => {
        // GIVEN
        service.find = jest.fn(transactionId => of(new HttpResponse({ body: { transactionId } })));
        mockActivatedRouteSnapshot.params = { transactionId: 'ABC' };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPlayersAudit = result;
        });

        // THEN
        expect(service.find).toBeCalledWith('ABC');
        expect(resultPlayersAudit).toEqual({ transactionId: 'ABC' });
      });

      it('should return new IPlayersAudit if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPlayersAudit = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultPlayersAudit).toEqual(new PlayersAudit());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { transactionId: 'ABC' };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPlayersAudit = result;
        });

        // THEN
        expect(service.find).toBeCalledWith('ABC');
        expect(resultPlayersAudit).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
