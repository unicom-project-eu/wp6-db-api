package it.datawizard.unicom.unicombackend.fhir;


import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import it.datawizard.unicom.unicombackend.fhir.resourceproviders.MedicationKnowledgeResourceProvider;
import it.datawizard.unicom.unicombackend.fhir.resourceproviders.SubstanceResourceProvider;
import it.datawizard.unicom.unicombackend.jpa.repository.SubstanceWithRolePaiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

@WebServlet(value = "/*", displayName = "fhir")
@Service
public class UnicomFHIRServlet extends RestfulServer {
    @Serial
    private static final long serialVersionUID = 1L;

    final private MedicationKnowledgeResourceProvider medicationKnowledgeResourceProvider;

    final private SubstanceResourceProvider substanceResourceProvider;

    @Autowired
    public UnicomFHIRServlet(MedicationKnowledgeResourceProvider medicationKnowledgeResourceProvider, SubstanceResourceProvider substanceResourceProvider) {
        this.medicationKnowledgeResourceProvider = medicationKnowledgeResourceProvider;
        this.substanceResourceProvider = substanceResourceProvider;
    }

    /**
     * The initialize method is automatically called when the servlet is starting up, so it can
     * be used to configure the servlet to define resource providers, or set up
     * configuration, interceptors, etc.
     */
    @Override
    protected void initialize() throws ServletException {
        /*
         * The servlet defines any number of resource providers, and
         * configures itself to use them by calling
         * setResourceProviders()
         */
        List<IResourceProvider> resourceProviders = new ArrayList<IResourceProvider>();
        resourceProviders.add(medicationKnowledgeResourceProvider);
        resourceProviders.add(substanceResourceProvider);
        setResourceProviders(resourceProviders);
    }
}
