package l3m.cyber.planner.services;

import l3m.cyber.planner.requests.PlannerParameter;
import l3m.cyber.planner.responses.PlannerResult;
import l3m.cyber.planner.utils.Planner;
import org.springframework.stereotype.Service;

@Service
public class PlannerService {

    public PlannerResult getResult(PlannerParameter params) {

        Planner pl = new Planner(params);
        pl.divise();
        pl.calculeTournees();
        pl.calculeLongTournees();
        return pl.result();

    }
}
